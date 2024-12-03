import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBankAccounts } from 'app/entities/accounts/bank-account/bank-account.reducer';
import { CurrencyType } from 'app/shared/model/enumerations/currency-type.model';
import { createEntity, getEntity, reset, updateEntity } from './internatonal-transfer.reducer';

export const InternatonalTransferUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bankAccounts = useAppSelector(state => state.gateway.bankAccount.entities);
  const internatonalTransferEntity = useAppSelector(state => state.gateway.internatonalTransfer.entity);
  const loading = useAppSelector(state => state.gateway.internatonalTransfer.loading);
  const updating = useAppSelector(state => state.gateway.internatonalTransfer.updating);
  const updateSuccess = useAppSelector(state => state.gateway.internatonalTransfer.updateSuccess);
  const currencyTypeValues = Object.keys(CurrencyType);

  const handleClose = () => {
    navigate(`/internatonal-transfer${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBankAccounts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.transactionDate = convertDateTimeToServer(values.transactionDate);

    const entity = {
      ...internatonalTransferEntity,
      ...values,
      bankAccount: bankAccounts.find(it => it.id.toString() === values.bankAccount?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          transactionDate: displayDefaultDateTime(),
        }
      : {
          currency: 'USD',
          ...internatonalTransferEntity,
          transactionDate: convertDateTimeFromServer(internatonalTransferEntity.transactionDate),
          bankAccount: internatonalTransferEntity?.bankAccount?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.accountsInternatonalTransfer.home.createOrEditLabel" data-cy="InternatonalTransferCreateUpdateHeading">
            <Translate contentKey="gatewayApp.accountsInternatonalTransfer.home.createOrEditLabel">
              Create or edit a InternatonalTransfer
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="internatonal-transfer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.accountsInternatonalTransfer.senderAccountNumber')}
                id="internatonal-transfer-senderAccountNumber"
                name="senderAccountNumber"
                data-cy="senderAccountNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternatonalTransfer.recipientIban')}
                id="internatonal-transfer-recipientIban"
                name="recipientIban"
                data-cy="recipientIban"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 15, message: translate('entity.validation.minlength', { min: 15 }) },
                  maxLength: { value: 34, message: translate('entity.validation.maxlength', { max: 34 }) },
                  pattern: { value: /^[A-Z0-9]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Z0-9]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternatonalTransfer.swiftCode')}
                id="internatonal-transfer-swiftCode"
                name="swiftCode"
                data-cy="swiftCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 11, message: translate('entity.validation.maxlength', { max: 11 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternatonalTransfer.recipientName')}
                id="internatonal-transfer-recipientName"
                name="recipientName"
                data-cy="recipientName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternatonalTransfer.amount')}
                id="internatonal-transfer-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternatonalTransfer.currency')}
                id="internatonal-transfer-currency"
                name="currency"
                data-cy="currency"
                type="select"
              >
                {currencyTypeValues.map(currencyType => (
                  <option value={currencyType} key={currencyType}>
                    {translate(`gatewayApp.CurrencyType.${currencyType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.accountsInternatonalTransfer.transactionDate')}
                id="internatonal-transfer-transactionDate"
                name="transactionDate"
                data-cy="transactionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternatonalTransfer.description')}
                id="internatonal-transfer-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 225, message: translate('entity.validation.maxlength', { max: 225 }) },
                }}
              />
              <ValidatedField
                id="internatonal-transfer-bankAccount"
                name="bankAccount"
                data-cy="bankAccount"
                label={translate('gatewayApp.accountsInternatonalTransfer.bankAccount')}
                type="select"
                required
              >
                <option value="" key="0" />
                {bankAccounts
                  ? bankAccounts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.iban}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/internatonal-transfer" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default InternatonalTransferUpdate;
