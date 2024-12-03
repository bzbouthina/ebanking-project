import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CardPayment from './card-payment';
import CardPaymentDetail from './card-payment-detail';
import CardPaymentUpdate from './card-payment-update';
import CardPaymentDeleteDialog from './card-payment-delete-dialog';

const CardPaymentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CardPayment />} />
    <Route path="new" element={<CardPaymentUpdate />} />
    <Route path=":id">
      <Route index element={<CardPaymentDetail />} />
      <Route path="edit" element={<CardPaymentUpdate />} />
      <Route path="delete" element={<CardPaymentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CardPaymentRoutes;
