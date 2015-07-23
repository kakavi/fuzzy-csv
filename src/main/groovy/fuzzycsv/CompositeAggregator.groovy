package fuzzycsv


class CompositeAggregator<T> implements Aggregator<T> {

    Map<String, Aggregator> aggregatorMap = [:]
    Closure cl
    String columnName

    CompositeAggregator() {
    }

    CompositeAggregator(String columnName, List<Aggregator> aggregators, Closure cl) {
        aggregators.each {
            aggregatorMap[it.columnName] = it
        }
        this.cl = cl
        this.columnName = columnName
    }


    @Override
    void setData(List<List> data) {
        aggregatorMap.each { key, value ->
            value.setData(data)
        }
    }

    @Override
    T getValue() {
        use(FxExtensions) {
           cl.call(aggregatorMap)
        }
    }


    static <T> CompositeAggregator<T> get(String columnName, List<Aggregator> aggregators, Closure cl) {
        return new CompositeAggregator<T>(columnName, aggregators, cl)
    }
}
