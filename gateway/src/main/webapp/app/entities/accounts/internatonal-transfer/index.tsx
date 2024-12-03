import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InternatonalTransfer from './internatonal-transfer';
import InternatonalTransferDetail from './internatonal-transfer-detail';
import InternatonalTransferUpdate from './internatonal-transfer-update';
import InternatonalTransferDeleteDialog from './internatonal-transfer-delete-dialog';

const InternatonalTransferRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InternatonalTransfer />} />
    <Route path="new" element={<InternatonalTransferUpdate />} />
    <Route path=":id">
      <Route index element={<InternatonalTransferDetail />} />
      <Route path="edit" element={<InternatonalTransferUpdate />} />
      <Route path="delete" element={<InternatonalTransferDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InternatonalTransferRoutes;
