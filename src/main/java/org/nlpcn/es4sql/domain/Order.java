package org.nlpcn.es4sql.domain;

/**
 * 排序规则
 * 
 * @author ansj
 *
 */
public class Order {
    private String nestedPath;
    private String name;
    private String type;

    private String lang;
    private String inline;
    private String scriptSortType;

    public Order(String nestedPath, String name, String type, String lang, String inline, String scriptSortType) {
        super();
        this.nestedPath = nestedPath;
        this.name = name;
        this.type = type;
        this.lang = lang;
        this.inline = inline;
        this.scriptSortType = scriptSortType;
    }

    public Order(String nestedPath, String name, String type) {
        this.nestedPath = nestedPath;
        this.name = name;
        this.type = type;
    }

    public String getNestedPath() {
        return nestedPath;
    }

    public void setNestedPath(String nestedPath) {
        this.nestedPath = nestedPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getInline() {
        return inline;
    }

    public void setInline(String inline) {
        this.inline = inline;
    }

    public String getScriptSortType() {
        return scriptSortType;
    }

    public void setScriptSortType(String scriptSortType) {
        this.scriptSortType = scriptSortType;
    }

}
