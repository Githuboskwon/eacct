package com.iljin.apiServer.ijeas.system.customer;

import static com.iljin.apiServer.ijeas.system.customer.QCustomer.customer;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerQdslRepositoryImpl implements CustomerQdslRepository{

    private final JPAQueryFactory queryFactory;
    private final Util util;

    @Override
    public Page<CustomerDto> getCustomerList(String value, Pageable pageable) {
        List<CustomerDto> list = queryFactory
            .select(new QCustomerDto(
                customer.compCd,
                customer.integrationVendorNum,
                customer.customerId,
                customer.customerNum,
                customer.customerName,
                customer.vatRegistrationNum
            ))
            .from(customer)
            .where(customer.compCd.eq(util.getLoginCompCd()),
                searchCondition(value))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults()
            .getResults();

        JPAQuery query = queryFactory
            .selectFrom(customer)
            .where(customer.compCd.eq(util.getLoginCompCd()),
                searchCondition(value));

        return PageableExecutionUtils.getPage(list, pageable, () -> query.fetch().size());
    }

    @Override
    public List<LocalBankDto> getLocalBankList(String value) {
        return queryFactory
            .select(new QLocalBankDto(
                customer.integrationVendorNum,
                customer.integrationVendorName
            ))
            .from(customer)
            .where(customer.venPayGroupLookupCd.eq("금융기관"),
                customer.compCd.eq("81"),
                localBankSearchCondition(value)
            )
            .fetch();
    }

    private BooleanBuilder searchCondition(String value) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(value)) {
            booleanBuilder.or(customer.customerNum.containsIgnoreCase(value))
                .or(customer.customerName.containsIgnoreCase(value));
        }
        return booleanBuilder;
    }
    
    private BooleanExpression venPayGroupLookupCdNotEq(String venPayGroupLookupCd) {
        return hasText(venPayGroupLookupCd) ? customer.venPayGroupLookupCd.ne(venPayGroupLookupCd) : null;
    }

    private BooleanBuilder localBankSearchCondition(String value) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(value)) {
            booleanBuilder.or(customer.integrationVendorNum.contains(value))
                .or(customer.integrationVendorName.contains(value));
        }
        return booleanBuilder;
    }

}
