import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <footer className="app-footer">
          <p>&copy; 2024 Zenith EBank. All Rights Reserved.</p>
        </footer>
      </Col>
    </Row>
  </div>
);

export default Footer;
