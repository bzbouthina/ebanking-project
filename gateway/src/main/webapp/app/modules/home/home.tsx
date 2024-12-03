import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className="home-container">
      <Row>
        <Col md="12" className="hero-section">
          <div className="hero-text">
            <h1 className="display-4">
              <Translate contentKey="home.title">Welcome to Zenith EBank!</Translate>
            </h1>
            <p className="lead">
              <Translate contentKey="home.subtitle">
                Experience a new era of digital banking. Secure, reliable, and tailored for you.
              </Translate>
            </p>
            {account?.login ? (
              <Alert color="success">
                <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                  You are logged in as {account.login}.
                </Translate>
              </Alert>
            ) : (
              <div className="cta-buttons">
                <Button color="primary" size="lg" tag={Link} to="/login">
                  <Translate contentKey="global.messages.info.authenticated.link">Sign In</Translate>
                </Button>
                <Button color="secondary" size="lg" tag={Link} to="/account/register" className="ml-3">
                  <Translate contentKey="global.messages.info.register.link">Register Now</Translate>
                </Button>
              </div>
            )}
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default Home;
