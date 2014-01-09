package com.example.utils;

import java.util.Comparator;

import com.example.dto.Record;

public class RecordDateComparator implements Comparator<Record>
{
	@Override
	public int compare(Record record1, Record record2) 
	{
		return record1.getDate().compareTo(record2.getDate());
		
	}

}
