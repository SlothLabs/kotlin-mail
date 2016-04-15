package io.github.slothLabs.mail.imap

import java.util.Date
import javax.mail.search.AndTerm
import javax.mail.search.ComparisonTerm
import javax.mail.search.NotTerm
import javax.mail.search.OrTerm
import javax.mail.search.ReceivedDateTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SentDateTerm
import javax.mail.search.SizeTerm

object ReceivedDate {
    infix fun eq(date: Date) = ReceivedDateTerm(ComparisonTerm.EQ, date)

    infix fun ne(date: Date) = ReceivedDateTerm(ComparisonTerm.NE, date)

    infix fun lt(date: Date) = ReceivedDateTerm(ComparisonTerm.LT, date)

    infix fun le(date: Date) = ReceivedDateTerm(ComparisonTerm.LE, date)

    infix fun gt(date: Date) = ReceivedDateTerm(ComparisonTerm.GT, date)

    infix fun ge(date: Date) = ReceivedDateTerm(ComparisonTerm.GE, date)

    infix fun between(range: ClosedRange<Date>) = (this ge range.start) and (this le range.endInclusive)
}

object SentDate {
    infix fun eq(date: Date) = SentDateTerm(ComparisonTerm.EQ, date)

    infix fun ne(date: Date) = SentDateTerm(ComparisonTerm.NE, date)

    infix fun lt(date: Date) = SentDateTerm(ComparisonTerm.LT, date)

    infix fun le(date: Date) = SentDateTerm(ComparisonTerm.LE, date)

    infix fun gt(date: Date) = SentDateTerm(ComparisonTerm.GT, date)

    infix fun ge(date: Date) = SentDateTerm(ComparisonTerm.GE, date)

    infix fun between(range: ClosedRange<Date>) = (this ge range.start) and (this le range.endInclusive)
}

object Size {
    infix fun eq(size: Int) = SizeTerm(ComparisonTerm.EQ, size)

    infix fun ne(size: Int) = SizeTerm(ComparisonTerm.NE, size)

    infix fun lt(size: Int) = SizeTerm(ComparisonTerm.LT, size)

    infix fun le(size: Int) = SizeTerm(ComparisonTerm.LE, size)

    infix fun gt(size: Int) = SizeTerm(ComparisonTerm.GT, size)

    infix fun ge(size: Int) = SizeTerm(ComparisonTerm.GE, size)

    infix fun between(range: ClosedRange<Int>) = (this ge range.start) and (this le range.endInclusive)
}

infix fun SearchTerm.and(other: SearchTerm): SearchTerm = AndTerm(this, other)

infix fun SearchTerm.or(other: SearchTerm): SearchTerm = OrTerm(this, other)

operator fun SearchTerm.not(): SearchTerm = if (this is NotTerm) term else NotTerm(this)

operator fun SearchTerm.plus(other: SearchTerm) = AndTerm(this, other)