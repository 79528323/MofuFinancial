package cn.mofufin.morf.ui.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class IndustryRateInfo {
    @DatabaseField
    public String code;
    @DatabaseField
    public String rate;
    @DatabaseField
    public String quota;

}
