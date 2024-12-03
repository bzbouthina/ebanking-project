import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import Customer from './customers/customer';
import Document from './customers/document';
import BankAccount from './accounts/bank-account';
import Localtransfer from './accounts/localtransfer';
import InternatonalTransfer from './accounts/internatonal-transfer';
import Payment from './payment/payment';
import QRPayment from './payment/qr-payment';
import BillPayment from './payment/bill-payment';
import CardPayment from './payment/card-payment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('gateway', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="customer/*" element={<Customer />} />
        <Route path="document/*" element={<Document />} />
        <Route path="bank-account/*" element={<BankAccount />} />
        <Route path="localtransfer/*" element={<Localtransfer />} />
        <Route path="internatonal-transfer/*" element={<InternatonalTransfer />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="qr-payment/*" element={<QRPayment />} />
        <Route path="bill-payment/*" element={<BillPayment />} />
        <Route path="card-payment/*" element={<CardPayment />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
