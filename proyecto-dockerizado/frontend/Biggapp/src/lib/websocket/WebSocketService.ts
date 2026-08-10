import { Client, type IMessage } from '@stomp/stompjs';

export interface WsMessagePayload {
    id: number;
    content: string;
    notificationId?: number;
    notificationType?: string;
}

type TopicHandler = (payload: WsMessagePayload) => void;

class WebSocketService {
    private client: Client | null = null;
    private handlers: TopicHandler[] = [];
    private pendingSubscriptions: TopicHandler[] = [];

    connect(token: string): void {
        if (this.client?.active) return;

        const wsProtocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
        const brokerURL = `${wsProtocol}://${window.location.host}/api/ws-native`;

        this.client = new Client({
            brokerURL,
            connectHeaders: { Authorization: `Bearer ${token}` },
            reconnectDelay: 5000,
            onConnect: () => {
                this.client!.subscribe('/topic/messages', (msg: IMessage) => {
                    const payload: WsMessagePayload = JSON.parse(msg.body);
                    this.handlers.forEach((h) => h(payload));
                });
                this.pendingSubscriptions.forEach((h) => this.handlers.push(h));
                this.pendingSubscriptions = [];
            },
        });

        this.client.activate();
    }

    disconnect(): void {
        this.client?.deactivate();
        this.client = null;
        this.handlers = [];
    }

    onMessage(handler: TopicHandler): () => void {
        if (this.client?.active) {
            this.handlers.push(handler);
        } else {
            this.pendingSubscriptions.push(handler);
        }
        return () => {
            this.handlers = this.handlers.filter((h) => h !== handler);
            this.pendingSubscriptions = this.pendingSubscriptions.filter((h) => h !== handler);
        };
    }

    get connected(): boolean {
        return this.client?.active ?? false;
    }
}

export const webSocketService = new WebSocketService();
