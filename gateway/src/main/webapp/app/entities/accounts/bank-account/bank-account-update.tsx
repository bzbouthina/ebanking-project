import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { AccountStatus } from 'app/shared/model/enumerations/account-status.model';
import { AccountType } from 'app/shared/model/enumerations/account-type.model';
import { createEntity, getEntity, reset, updateEntity } from './bank-account.reducer';

export const BankAccountUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bankAccountEntity = useAppSelector(state => state.gateway.bankAccount.entity);
  const loading = useAppSelector(state => state.gateway.bankAccount.loading);
  const updating = useAppSelector(state => state.gateway.bankAccount.updating);
  const updateSuccess = useAppSelector(state => state.gateway.bankAccount.updateSuccess);
  const accountStatusValues = Object.keys(AccountStatus);
  const accountTypeValues = Object.keys(AccountType);

  const handleClose = () => {
    navigate(`/bank-account${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.balance !== undefined && typeof values.balance !== 'number') {
      values.balance = Number(values.balance);
    }

    const entity = {
      ...bankAccountEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'ACTIVE',
          accountType: 'CHEKING',
          ...bankAccountEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.accountsBankAccount.home.createOrEditLabel" data-cy="BankAccountCreateUpdateHeading">
            <Translate contentKey="gatewayApp.accountsBankAccount.home.createOrEditLabel">Create or edit a BankAccount</Translate>
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
                  id="bank-account-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.login')}
                id="bank-account-login"
                name="login"
                data-cy="login"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.accountNumber')}
                id="bank-account-accountNumber"
                name="accountNumber"
                data-cy="accountNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 10, message: translate('entity.validation.minlength', { min: 10 }) },
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                  pattern: { value: /^[0-9A-Z]+$/, message: translate('entity.validation.pattern', { pattern: '^[0-9A-Z]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.iban')}
                id="bank-account-iban"
                name="iban"
                data-cy="iban"
                type="text"
                validate={{
                  minLength: { value: 15, message: translate('entity.validation.minlength', { min: 15 }) },
                  maxLength: { value: 34, message: translate('entity.validation.maxlength', { max: 34 }) },
                  pattern: { value: /^[A-Z0-9]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Z0-9]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.balance')}
                id="bank-account-balance"
                name="balance"
                data-cy="balance"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.currency')}
                id="bank-account-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 3, message: translate('entity.validation.maxlength', { max: 3 }) },
                  pattern: { value: /^[A-Z]{3}$/, message: translate('entity.validation.pattern', { pattern: '^[A-Z]{3}$' }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.openingDate')}
                id="bank-account-openingDate"
                name="openingDate"
                data-cy="openingDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.status')}
                id="bank-account-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {accountStatusValues.map(accountStatus => (
                  <option value={accountStatus} key={accountStatus}>
                    {translate(`gatewayApp.AccountStatus.${accountStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.accountType')}
                id="bank-account-accountType"
                name="accountType"
                data-cy="accountType"
                type="select"
              >
                {accountTypeValues.map(accountType => (
                  <option value={accountType} key={accountType}>
                    {translate(`gatewayApp.AccountType.${accountType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.accountsBankAccount.branch')}
                id="bank-account-branch"
                name="branch"
                data-cy="branch"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bank-account" replace color="info">
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

export default BankAccountUpdate;
