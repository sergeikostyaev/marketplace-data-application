package gerbarika.web.application.mapper

interface Mapper<P, R> {
    fun toDto(p: P): R;
}