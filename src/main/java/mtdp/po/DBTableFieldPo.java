package mtdp.po;

import java.io.Serializable;

/**
 * Created by king_luffy on 2017/9/6.
 */
public class DBTableFieldPo implements Serializable {

    String dbTableId;
    String fieldName;
    String fieldId;
    String filedType;
    String filedLength;

    public String getDbTableId() {
        return dbTableId;
    }

    public void setDbTableId(String dbTableId) {
        this.dbTableId = dbTableId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFiledType() {
        return filedType;
    }

    public void setFiledType(String filedType) {
        this.filedType = filedType;
    }

    public String getFiledLength() {
        return filedLength;
    }

    public void setFiledLength(String filedLength) {
        this.filedLength = filedLength;
    }
}
