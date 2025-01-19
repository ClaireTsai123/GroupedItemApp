package com.tsai.fetchrewardscodingexcercise.data.transformer

interface Transformer<Source, Target> {
    fun transform(source: Source): Target
}
