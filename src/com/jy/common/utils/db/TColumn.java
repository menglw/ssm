package com.jy.common.utils.db;

import java.io.Serializable;

/**
 * 数据库列类 License：jynet
 */
public class TColumn implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 列的名称
	 */
	private String name;
	/**
	 * 列的 SQL类型
	 */
	private String type;
	/**
	 * 对于数值型数据，是指最大精度。对于字符型数据，是指字符串长度。对于日期时间的数据类型，
	 * 是指 String 表示形式的字符串长度（假定为最大允许的小数秒组件）。对于二进制型数据，
	 * 是指字节长度。对于 ROWID 数据类型，是指字节长度。对于其列大小不可用的数据类型，则为 0
	 */
	private int precision;
	/**
	 * 列的小数点右边的位数
	 */
	private int scale;
	/**
	 * 是否为null：<br/>
	 * 0指示列中可能不允许使用 NULL值。 <br/>
	 * 1指示列中明确允许使用 NULL值。<br/>
     * 2指示不知道列是否可为 null。 <br/>
	 */
	private int isNullable;

	public TColumn() {
		super();
	}

	public TColumn(String name, String type, int precision, int scale,int isNullable) {
		super();
		this.name = name;
		this.type = type;
		this.precision = precision;
		this.scale = scale;
		this.isNullable = isNullable;
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

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(int isNullable) {
		this.isNullable = isNullable;
	}

	@Override
	public String toString() {
		return "TColumn [name=" + name + ", type=" + type + ", precision=" + precision + ", scale=" + scale
				+ ", isNullable=" + isNullable + "]";
	}

}
