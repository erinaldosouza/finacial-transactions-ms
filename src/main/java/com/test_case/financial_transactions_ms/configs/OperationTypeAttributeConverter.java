package com.test_case.financial_transactions_ms.configs;


import com.test_case.financial_transactions_ms.enums.OperationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OperationTypeAttributeConverter implements AttributeConverter<OperationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OperationType operationType) {
        return (operationType == null) ? null : operationType.getOperationId();
    }

    @Override
    public OperationType convertToEntityAttribute(Integer dbData) {
        return (dbData == null) ? null : OperationType.fromValue(dbData);
    }
}

