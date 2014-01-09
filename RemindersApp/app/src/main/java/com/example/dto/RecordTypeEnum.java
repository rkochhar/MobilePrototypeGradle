package com.example.dto;

public enum RecordTypeEnum {

	BIRTHDAY(1),ANNIVERSARY(2);
	
	private int type;
	
	private RecordTypeEnum(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public static String getEnumValue(int type)
	{
		for(RecordTypeEnum recordTypeEnum : RecordTypeEnum.values())
		{
			if(type == recordTypeEnum.getType())
			{
				return recordTypeEnum.name();
			}
		}
		return null;
	}
}
