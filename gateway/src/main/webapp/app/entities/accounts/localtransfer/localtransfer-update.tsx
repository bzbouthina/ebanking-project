import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBankAccounts } from 'app/entities/accounts/bank-account/bank-account.reducer';
import { createEntity, getEntity, reset, updateEntity } from './localtransfer.reducer';

export const LocaltransferUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bankAccounts = useAppSelector(state => state.gateway.bankAccount.entities);
  const localtransferEntity = useAppSelector(state => state.gateway.localtransfer.entity);
  const loading = useAppSelector(state => state.gateway.localtransfer.loading);
  const updating = useAppSelector(state => state.gateway.localtransfer.updating);
  const updateSuccess = useAppSelector(state => state.gateway.localtransfer.updateSuccess);

  const handleClose = () => {
    navigate(`/localtransfer${location.search}`);
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
      ...localtransferEntity,
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
          ...localtransferEntity,
          transactionDate: convertDateTimeFromServer(localtransferEntity.transactionDate),
          bankAccount: localtransferEntity?.bankAccount?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.accountsLocaltransfer.home.createOrEditLabel" data-cy="LocaltransferCreateUpdateHeading">
            <Translate contentKey="gatewayApp.accountsLocaltransfer.home.createOrEditLabel">Create or edit a Localtransfer</Translate>
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
                  id="localtransfer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.accountsLocaltransfer.senderAccountNumber')}
                id="localtransfer-senderAccountNumber"
                name="senderAccountNumber"
                data-cy="senderAccountNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocaltransfer.recipientAccountNumber')}
                id="localtransfer-recipientAccountNumber"
                name="recipientAccountNumber"
                data-cy="recipientAccountNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocaltransfer.recipientBankName')}
                id="localtransfer-recipientBankName"
                name="recipientBankName"
                data-cy="recipientBankName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocaltransfer.recipientBankBranch')}
                id="localtransfer-recipientBankBranch"
                name="recipientBankBranch"
                data-cy="recipientBankBranch"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocaltransfer.amount')}
                id="localtransfer-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocaltransfer.transactionDate')}
                id="localtransfer-transactionDate"
                name="transactionDate"
                data-cy="transactionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocaltransfer.description')}
                id="localtransfer-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 225, message: translate('entity.validation.maxlength', { max: 225 }) },
                }}
              />
              <ValidatedField
                id="localtransfer-bankAccount"
                name="bankAccount"
                data-cy="bankAccount"
                label={translate('gatewayApp.accountsLocaltransfer.bankAccount')}
                type="select"
                required
              >
                <option value="" key="0" />
                {bankAccounts
                  ? bankAccounts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.accountNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/localtransfer" replace color="info">
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

export default LocaltransferUpdate;
