import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Localtransfer from './localtransfer';
import LocaltransferDetail from './localtransfer-detail';
import LocaltransferUpdate from './localtransfer-update';
import LocaltransferDeleteDialog from './localtransfer-delete-dialog';

const LocaltransferRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Localtransfer />} />
    <Route path="new" element={<LocaltransferUpdate />} />
    <Route path=":id">
      <Route index element={<LocaltransferDetail />} />
      <Route path="edit" element={<LocaltransferUpdate />} />
      <Route path="delete" element={<LocaltransferDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LocaltransferRoutes;
