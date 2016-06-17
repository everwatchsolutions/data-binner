# Data Binner

The Data Binner is a generic framework that will take input data, then generate unique key names that represent a bin name at a specific level. It is often helpful to sort data into differnt bins in order to build things like metrics or charts with your data.  

## Data Flow

Data is provided to a Binner along with a bin name and optional other configuration. The Binner then extracts the value of the provided data you would like to bin and generates a number of bin names that can be used for your own purposes.  You could for example use the bin names to increment counters with those names.  

## Converting Data

The Data Binner currently supports json data as an input, but provides an interface to be able to handle other types of input data. See the `DataExtractor` interface and the `JsonDataExtractor` implmentation for more information. 

### Json Data

Our main supported format of input data at the moment is Json data.  The `JsonDataExtractor` supports the ability to extract data from nested properties by specifying the field name in `dot` form. So if you had data like:

```
{
    "car": {
        "make": "Subaru",
        "model": "Impreza WRX"
    }
}
```
And you wanted to create bins for the `make` of the cars, you would need to specify a `dataFieldName` of: `car.make`

More on the `dataFieldName` property is contained in the Binners section. 

## Binners

The Data Binner provides a few Binners out of the box. With each Binner, you must provide a `binName`. If the name of the field you are trying to count in your data is different than the `binName` then you may provide an optional `dataFieldName`.  The simpliest example would be a Literal Binner like so. 

Using a bin name the same as the field name:

```
{
    "action": "Punch"
}
```

With the Binner configured like:
```
Binner binner = new LiteralBinner("action");
List<String> bins = binner.generateBinNames(jsonString);
```

If you want your bin name to differ from your data's field name:
```
Binner binner = new LiteralBinner("myOtherName", "action");
List<String> bins = binner.generateBinNames(jsonString);
```

### Literal Binner

The Literal Binner looks at literal (or string) values within your data.  For instance, if you had a field named "action" and it had values like "Punch", "Kick", "Run" then the Literal Binner would generate bin names for those literal values. An example:

If you had input data:

```
{
    "action": "Punch"
}
```

The Literal Binner configured like:

```
Binner binner = new LiteralBinner("action");
List<String> bins = binner.generateBinNames(jsonString);
```
Would generate Bins like:

```
action.All
action.Punch
```

### Date Binner

The Date Binner takes provided Date values and will break it out into multiple Date Bins at different Date Granularities. An example:

Input data:

```
{
    "myDate": "2012-04-23T18:25:43.511Z"
}
```

The Date Binner would be configured like:

```
Binner binner = new DateBinner("date", "myDate", DateGranularity.MIN);
List<String> bins = binner.generateBinNames(jsonString);
```

Would generate Bins like:

```
date.All
date.2012
date.201225
date.20120423
date.2012042306
date.201204230625
```

### Numeric Binner

The Numeric Binner takes provided numeric values and will bin it at different bin levels in powers of 10. Example:

Input data:

```
{
    "airSpeed": 456.5
}
```

The Binner would be configured like:

```
Binner binner = new NumericBinner("airSpeed", 10);
List<String> bins = binner.generateBinNames(jsonString);
```

Would generate Bins like:

```
airSpeed.All
airSpeed.456-457
airSpeed.450-460
airSpeed.400-500
airSpeed.0-1000
airSpeed.0-10000
airSpeed.0-100000
airSpeed.0-1000000
airSpeed.0-10000000
airSpeed.0-100000000
airSpeed.0-1000000000
```

Note the Numeric Binner takes an optional "maxLevel" as a parameter to the constructor. The Bins will grow by powers of 10 until it reaches your specified maxLevel (10^maxLevel).
 
### GeoTile Binner

The Geo Tile Binner will take provided location data and bin it into Geographic Tiles, like those used in a Web Mercator Map Tiling Service.  It will generate Bin names with a `"zoomLevel"-"xCoord"-"yCoord"` system. An example:

Input data:

```
{
    "point": {
        "x": 88.2,
        "y": -99.3
    }
}
```

The Binner would be configured like:

```
Binner binner = new GeoTileBinner("geo", "point", 10, "x", "y");
List<String> bins = binner.generateBinNames(jsonString);
```

Would generate Bins like:

```
geo.All
geo.0-0-0
geo.1-0-0
geo.2-0-0
geo.3-1-0
geo.4-3-0
geo.5-7-0
geo.6-14-0
geo.7-28-0
geo.8-57-0
geo.9-114-0
```
Note the GeoTileBinner takes a number of optional parameters:

- A maxLevel which is the max zoom level to generate bins down to
- The name of the Latitude parameter within your data if your data is provided as a map/object like the example above. If you do not provide a name, we assume the name of the property is called `lat`
- The name of the Longitude parameter within your data if your data is provided as a map/object like the example above. If you do not provide a name, we assume the name of the property is called `lon`

The GeoTileBinner will also handle data in the form of an array (or List) with lat, lon like so:

```
{
    "point": [45.0, 45.0]
}
```
And a Binner like:

```
Binner binner = new GeoTileBinner("geo", "point");
List<String> bins = binner.generateBinNames(jsonString);
```