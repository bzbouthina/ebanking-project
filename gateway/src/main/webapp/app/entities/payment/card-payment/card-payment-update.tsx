import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPayments } from 'app/entities/payment/payment/payment.reducer';
import { createEntity, getEntity, reset, updateEntity } from './card-payment.reducer';

export const CardPaymentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const payments = useAppSelector(state => state.gateway.payment.entities);
  const cardPaymentEntity = useAppSelector(state => state.gateway.cardPayment.entity);
  const loading = useAppSelector(state => state.gateway.cardPayment.loading);
  const updating = useAppSelector(state => state.gateway.cardPayment.updating);
  const updateSuccess = useAppSelector(state => state.gateway.cardPayment.updateSuccess);

  const handleClose = () => {
    navigate(`/card-payment${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPayments({}));
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
    values.paymentDate = convertDateTimeToServer(values.paymentDate);

    const entity = {
      ...cardPaymentEntity,
      ...values,
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
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
          paymentDate: displayDefaultDateTime(),
        }
      : {
          ...cardPaymentEntity,
          paymentDate: convertDateTimeFromServer(cardPaymentEntity.paymentDate),
          payment: cardPaymentEntity?.payment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.paymentCardPayment.home.createOrEditLabel" data-cy="CardPaymentCreateUpdateHeading">
            <Translate contentKey="gatewayApp.paymentCardPayment.home.createOrEditLabel">Create or edit a CardPayment</Translate>
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
                  id="card-payment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.paymentCardPayment.cardNumber')}
                id="card-payment-cardNumber"
                name="cardNumber"
                data-cy="cardNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /^[0-9]{16}$/, message: translate('entity.validation.pattern', { pattern: '^[0-9]{16}$' }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentCardPayment.cardExpiryDate')}
                id="card-payment-cardExpiryDate"
                name="cardExpiryDate"
                data-cy="cardExpiryDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentCardPayment.cardHolderName')}
                id="card-payment-cardHolderName"
                name="cardHolderName"
                data-cy="cardHolderName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentCardPayment.cvv')}
                id="card-payment-cvv"
                name="cvv"
                data-cy="cvv"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /^[0-9]{3,4}$/, message: translate('entity.validation.pattern', { pattern: '^[0-9]{3,4}$' }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentCardPayment.amount')}
                id="card-payment-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentCardPayment.paymentDate')}
                id="card-payment-paymentDate"
                name="paymentDate"
                data-cy="paymentDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.paymentCardPayment.description')}
                id="card-payment-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                id="card-payment-payment"
                name="payment"
                data-cy="payment"
                label={translate('gatewayApp.paymentCardPayment.payment')}
                type="select"
                required
              >
                <option value="" key="0" />
                {payments
                  ? payments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.paymentId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/card-payment" replace color="info">
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

export default CardPaymentUpdate;
