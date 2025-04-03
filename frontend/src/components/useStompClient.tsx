"use client";

import { Client } from "@stomp/stompjs";
import { useEffect, useState } from "react";

export default function useStompClient(isAuth: boolean) {
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    if (!isAuth || stompClient) {
      return;
    }
    const client = new Client({
      brokerURL: "ws://localhost:9000/ws",
      onConnect: () => {
        client.subscribe("/topic/greeting", (message) => {
          console.log(message.body);
        });
        client.publish({
          destination: "/topic/greeting",
          body: "Hallo von socket",
        });
      },
    });
    client.activate();
    console.log("Connected");
    setStompClient(client);
  }, [isAuth, stompClient]);
  return [stompClient];
}
