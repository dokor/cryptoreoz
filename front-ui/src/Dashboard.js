import * as React from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { Title } from 'react-admin';
import StockChart from './StockChart';
import ReconnectableWebSocket from './socket/ReconnectableWebSocket';
import { useState } from 'react';

export default () => {
  const [btcValue, setBtcValue] = useState(0);

  const socket = new ReconnectableWebSocket({
    url: `${document.location.protocol === 'https:' ? 'wss://' : 'ws://'}${document.location.host}/socket`,
    onOpen: (e) => {
      console.log('OPEN !', e)
    },
    onMessage: (e) => {
      setBtcValue(parseFloat(e.data.substring(8, e.data.length)))
    },
    onClose: (e) => {
      console.log('CLOSE !', e)
    },
  });

  socket.open();

  return (
    <Card>
      <Title title="Welcome to the administration"/>
      <CardContent>Valeur du BTCUSDT recuperée par websocket depuis l'API Binance : {btcValue}</CardContent>
      <StockChart/>
    </Card>
  )
};
