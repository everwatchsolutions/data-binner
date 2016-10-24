/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.data.binner;

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
    private static final String ISO8601_DATE_TEMPLATE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    //instantiating each time down below due to thread-safe problems that SimpleDateFormat has trouble with:  http://stackoverflow.com/questions/11368632/multiple-exceptions-thrown-parsing-date-string
    private SimpleDateFormat iso8601Format;
    private FastDateFormat yearFormat = FastDateFormat.getInstance("yyyy");
    private FastDateFormat yearMonthFormat = FastDateFormat.getInstance("yyyyMM");
    private FastDateFormat yearMonthDayFormat = FastDateFormat.getInstance("yyyyMMdd");
    private FastDateFormat yearMonthDayHourFormat = FastDateFormat.getInstance("yyyyMMddHH");
    private FastDateFormat yearMonthDayHourMinFormat = FastDateFormat.getInstance("yyyyMMddHHmm");
    private FastDateFormat yearMonthDayHourMinSecFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");
    private FastDateFormat yearMonthDayHourMinSecMSFormat = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
    
    private DateGranularity granularity;
    private Map<DateGranularity, List<FastDateFormat>> granToSDFMap;
    
    public DateBinner(String countName, DateGranularity granularity) {
        this(countName, countName, granularity);
    }
    public DateBinner(String countName, String dataFieldName, DateGranularity granularity) {
        super(countName, dataFieldName);
        this.granularity = granularity;
        
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
        if (value == null) {
            return binNames;
        }
        
        Date date = null;
        if (Date.class.isAssignableFrom(value.getClass())) {
            date = (Date)value;
        } else if (String.class.isAssignableFrom(value.getClass())) {
            if (((String)value).isEmpty()) {
                log.debug("Unable to generate date binNames for empty value");
                return binNames;
            }
            try {
                iso8601Format = new SimpleDateFormat(ISO8601_DATE_TEMPLATE);
                date = iso8601Format.parse(value.toString());
            } catch (Exception ex) {
                log.warn("String value [ " + value + " ] is not a Date format we support. Please provide dates in ISO8601 format", ex);
            }
        } else if (Long.class.isAssignableFrom(value.getClass())) {
            date = new Date((Long) value);
        } else {
            //we don't know how to handle anything but dates...
            log.warn("Value is not a Date or String [ " + value + " ] is of type [ " + value.getClass() + " ]");
        }
        
        if (date != null) {
            final Date finalDate = date;
            List<FastDateFormat> formatters = granToSDFMap.get(granularity);
            formatters.stream().forEach((sdf) -> {
                try {
                    String dateValue = sdf.format(finalDate);
                    binNames.add(getBinName() + "." + dateValue);
                } catch (Exception e) {
                    log.debug("Unable to format date [ " + finalDate + " ] with formatter [ " + sdf.getPattern() + " ]");
                }
            });
        }
        return binNames;
    }
    
}
