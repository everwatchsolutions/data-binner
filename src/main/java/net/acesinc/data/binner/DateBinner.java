/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public class DateBinner extends Binner {
    private static Logger log = LoggerFactory.getLogger(DateBinner.class);
    
    private SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private FastDateFormat yearFormat = FastDateFormat.getInstance("yyyy");
    private FastDateFormat yearMonthFormat = FastDateFormat.getInstance("yyyymm");
    private FastDateFormat yearMonthDayFormat = FastDateFormat.getInstance("yyyyMMdd");
    private FastDateFormat yearMonthDayHourFormat = FastDateFormat.getInstance("yyyyMMddhh");
    private FastDateFormat yearMonthDayHourMinFormat = FastDateFormat.getInstance("yyyyMMddhhmm");
    private FastDateFormat yearMonthDayHourMinSecFormat = FastDateFormat.getInstance("yyyyMMddhhmmss");
    private FastDateFormat yearMonthDayHourMinSecMSFormat = FastDateFormat.getInstance("yyyyMMddhhmmssSSS");
    
    private DateGranularity granulatiry;
    private Map<DateGranularity, List<FastDateFormat>> granToSDFMap;
    
    public DateBinner(String countName, DateGranularity granularity) {
        this(countName, countName, granularity);
    }
    public DateBinner(String countName, String dataFieldName, DateGranularity granularity) {
        super(countName, dataFieldName);
        this.granulatiry = granularity;
        
        granToSDFMap = new HashMap<>();
        granToSDFMap.put(DateGranularity.YEAR, Arrays.asList(yearFormat));
        granToSDFMap.put(DateGranularity.MONTH, Arrays.asList(yearFormat, yearMonthFormat));
        granToSDFMap.put(DateGranularity.DAY, Arrays.asList(yearFormat, yearMonthFormat, yearMonthDayFormat));
        granToSDFMap.put(DateGranularity.HOUR, Arrays.asList(yearFormat, yearMonthFormat, yearMonthDayFormat, yearMonthDayHourFormat));
        granToSDFMap.put(DateGranularity.MIN, Arrays.asList(yearFormat, yearMonthFormat, yearMonthDayFormat, yearMonthDayHourFormat, yearMonthDayHourMinFormat));
        granToSDFMap.put(DateGranularity.SEC, Arrays.asList(yearFormat, yearMonthFormat, yearMonthDayFormat, yearMonthDayHourFormat, yearMonthDayHourMinFormat, yearMonthDayHourMinSecFormat));
        granToSDFMap.put(DateGranularity.MSEC, Arrays.asList(yearFormat, yearMonthFormat, yearMonthDayFormat, yearMonthDayHourFormat, yearMonthDayHourMinFormat, yearMonthDayHourMinSecFormat, yearMonthDayHourMinSecMSFormat));
    }
    
    @Override
    protected List<String> generateBinNamesForData(Object value) {
        List<String> binNames = new ArrayList<>();
        Date date = null;
        if (Date.class.isAssignableFrom(value.getClass())) {
            date = (Date)value;
        } else if (String.class.isAssignableFrom(value.getClass())) {
            try {
                date = iso8601Format.parse(value.toString());
            } catch (ParseException ex) {
                log.warn("String value is not a Date format we support. Please provide dates in ISO8601 format", ex);
            }
        } else if (Long.class.isAssignableFrom(value.getClass())) {
            date = new Date((Long) value);
        } else {
            //we don't know how to handle anything but dates...
            log.warn("Value is not a Date or String [ " + value + " ] is of type [ " + value.getClass() + " ]");
        }
        
        if (date != null) {
            final Date finalDate = date;
            List<FastDateFormat> formatters = granToSDFMap.get(granulatiry);
            formatters.stream().forEach((sdf) -> {
                String dateValue = sdf.format(finalDate);
                binNames.add(getBinName() + "." + dateValue);
            });
        }
        return binNames;
    }
    
}
