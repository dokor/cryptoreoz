import * as React from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { Title } from 'react-admin';
import StockChart from './StockChart';

export default () => (
  <Card>
    <Title title="Welcome to the administration"/>
    <CardContent>Lorem ipsum sic dolor amet...</CardContent>
    <StockChart/>
  </Card>
);