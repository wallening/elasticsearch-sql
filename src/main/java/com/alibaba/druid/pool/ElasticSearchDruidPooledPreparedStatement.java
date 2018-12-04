package com.alibaba.druid.pool;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.plugin.nlpcn.QueryActionElasticExecutor;
import org.elasticsearch.plugin.nlpcn.executors.CSVResult;
import org.elasticsearch.plugin.nlpcn.executors.CSVResultsExtractor;
import org.elasticsearch.plugin.nlpcn.executors.CsvExtractorException;
import org.nlpcn.es4sql.SearchDao;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.jdbc.ObjectResult;
import org.nlpcn.es4sql.jdbc.ObjectResultsExtractor;
import org.nlpcn.es4sql.query.QueryAction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

/**
 * Created by allwefantasy on 8/30/16.
 */
public class ElasticSearchDruidPooledPreparedStatement extends DruidPooledPreparedStatement {

	Client client = null;

	public ElasticSearchDruidPooledPreparedStatement(DruidPooledConnection conn, PreparedStatementHolder holder)
			throws SQLException {
		super(conn, holder);
		this.client = ((ElasticSearchConnection) conn.getConnection()).getClient();
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		checkOpen();

		incrementExecuteCount();
		transactionRecord(getSql());

		oracleSetRowPrefetch();

		conn.beforeExecute();
		try {

			String sql = getSql();
			boolean includeScore = false;
			boolean includeIndex = sql.indexOf("_index") > -1 ? true : false;
			boolean includeType = sql.indexOf("_type") > -1 ? true : false;
			boolean includeId = sql.indexOf("_id") > -1 ? true : false;

			ObjectResult extractor = getObjectResult(true, sql, includeScore, includeIndex, includeType, includeId);
			List<String> headers = extractor.getHeaders();
			List<List<Object>> lines = extractor.getLines();

			ResultSet rs = new ElasticSearchResultSet(this, headers, lines);

			if (rs == null) {
				return null;
			}

			DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
			addResultSetTrace(poolableResultSet);

			return poolableResultSet;
		} catch (Throwable t) {
			throw checkException(t);
		} finally {
			conn.afterExecute();
		}
	}

	private ObjectResult getObjectResult(boolean flat, String query, boolean includeScore, boolean includeIndex,
			boolean includeType, boolean includeId)
			throws SqlParseException, SQLFeatureNotSupportedException, Exception, CsvExtractorException {
		SearchDao searchDao = new org.nlpcn.es4sql.SearchDao(client);

		// String rewriteSQL = searchDao.explain(getSql()).explain().explain();

		QueryAction queryAction = searchDao.explain(query);
		Object execution = QueryActionElasticExecutor.executeAnyAction(searchDao.getClient(), queryAction);
		return new ObjectResultsExtractor(includeScore, includeIndex, includeType, includeId).extractResults(execution,
				flat);
	}

	@Override
	public int executeUpdate() throws SQLException {
		throw new SQLException("executeUpdate not support in ElasticSearch");
	}
}
