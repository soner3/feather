import type { ReactNode } from "react";
import { StoreProvider } from "./StoreProvider";

import "./globals.css";
import AuthenticationProvider from "../components/AuthenticationProvider";

interface Props {
  readonly children: ReactNode;
}

export default function RootLayout({ children }: Props) {
  return (
    <AuthenticationProvider>
      <StoreProvider>
        <html lang="en">
          <body>{children}</body>
        </html>
      </StoreProvider>
    </AuthenticationProvider>
  );
}
