export class ConnectionState {
    static CONNECTING = 0;
    static OPEN = 1;
    static CLOSING = 2;
    static CLOSED = 3;
}

export default class ReconnectableWebSocket {
    type: string;
    url: string;
    onOpen: (ev: Event) => any;
    onMessage: (ev: MessageEvent) => any;
    onClose: (ev: CloseEvent) => any;
    connection: any;

    constructor(params: any) {
        this.type = params.type;
        this.url = params.url;
        this.onOpen = params.onOpen;
        this.onMessage = params.onMessage;
        this.onClose = params.onClose;
        this.connection = null;
    }

    open = () => {
        if (this.isConnectedOrConnecting()) {
            console.log('Tentative d\'ouverture d\'une nouvelle connexion alors que la connexion est déjà ouverte/en ouverture');
            return;
        }

        this.connection = new WebSocket(this.url);
        this.connection.onopen = this.onOpen;
        this.connection.onmessage = (e: MessageEvent) => {
            const newEvent = {
                ...e,
                data: e.data.replace(this.type + ":", '')
            };
            return this.onMessage(newEvent)
        }

        this.connection.onclose = this.onClose;
    };

    isOpen = () => this.connection && this.connection.readyState === ConnectionState.OPEN;

    isConnectedOrConnecting = () => this.connection
        && (this.connection.readyState === ConnectionState.CONNECTING
            || this.connection.readyState === ConnectionState.OPEN);

    send = (message: string) => {
        if (this.isOpen()) {
            try {
                // $FlowFixMe: la connexion ne peut pas être nulle => test fait dans la méthode isOpen()
                this.connection.send(message);
            } catch (e) {
                console.log(`Message ${message} not sent`, e);
                return false;
            }
            return true;
        }
        console.log(`Message ${message} not sent since the connection is not initialized yet`);
        return false;
    };

    close = () => {
        if (this.connection) {
            this.connection.close();
            this.connection = null;
        }
    };
}
