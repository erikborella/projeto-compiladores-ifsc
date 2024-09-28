const WEB_SOCKET_ADDRESS = "ws://localhost:8080/compiler/runner/ws";

const getRunnerWebSocket = (codeId: string): WebSocket => {
    const url = new URL(WEB_SOCKET_ADDRESS);
    url.searchParams.set('codeId', codeId);

    const ws = new WebSocket(url.toString());

    return ws;
};


export default { getRunnerWebSocket };