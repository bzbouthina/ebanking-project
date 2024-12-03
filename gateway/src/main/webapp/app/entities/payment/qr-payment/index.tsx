import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QRPayment from './qr-payment';
import QRPaymentDetail from './qr-payment-detail';
import QRPaymentUpdate from './qr-payment-update';
import QRPaymentDeleteDialog from './qr-payment-delete-dialog';

const QRPaymentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QRPayment />} />
    <Route path="new" element={<QRPaymentUpdate />} />
    <Route path=":id">
      <Route index element={<QRPaymentDetail />} />
      <Route path="edit" element={<QRPaymentUpdate />} />
      <Route path="delete" element={<QRPaymentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QRPaymentRoutes;
