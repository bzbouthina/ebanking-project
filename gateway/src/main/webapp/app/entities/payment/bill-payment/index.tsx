import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BillPayment from './bill-payment';
import BillPaymentDetail from './bill-payment-detail';
import BillPaymentUpdate from './bill-payment-update';
import BillPaymentDeleteDialog from './bill-payment-delete-dialog';

const BillPaymentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BillPayment />} />
    <Route path="new" element={<BillPaymentUpdate />} />
    <Route path=":id">
      <Route index element={<BillPaymentDetail />} />
      <Route path="edit" element={<BillPaymentUpdate />} />
      <Route path="delete" element={<BillPaymentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BillPaymentRoutes;
