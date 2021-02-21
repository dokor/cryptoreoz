import * as React from 'react';
import { useEffect, useState } from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { Title } from 'react-admin';
import StockChart from './StockChart';
import ReconnectableWebSocket from './socket/ReconnectableWebSocket';

export default () => {
  const [btcValue, setBtcValue] = useState(0);

  useEffect(() => {
    const newSocket = new ReconnectableWebSocket({
      type: 'FINANCE',
      url: `${document.location.protocol === 'https:' ? 'wss://' : 'ws://'}${document.location.host}/socket`,
      onOpen: (e) => {
        console.log('open socket', e)
      },
      onMessage: (e) => {
        const [type, value] = e.data.split(':');
        if (type === 'btcusdt') {
          setBtcValue(parseFloat(value))
        }
      },
      onClose: (e) => {
        console.log('close socket', e)
      },
    });
    newSocket.open();

    return function cleanup() {
      newSocket.close();
    }
  }, []);
  return (
    <Card>
      <Title title="Welcome to the administration"/>
      <CardContent>Valeur du BTCUSDT recuperée par websocket depuis l'API Binance : {btcValue}</CardContent>
      <StockChart/>
    </Card>
  )
};
