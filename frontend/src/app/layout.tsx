import type { ReactNode } from "react";
import { StoreProvider } from "./StoreProvider";

import "./globals.css";
import AuthenticationProvider from "../components/AuthenticationProvider";
import Navigation from "../components/Navigation";

interface Props {
  readonly children: ReactNode;
}

export default function RootLayout({ children }: Props) {
  return (
    <AuthenticationProvider>
      <StoreProvider>
        <html lang="en" className="h-full bg-gray-100">
          <body className="h-full">
            <Navigation>{children}</Navigation>
          </body>
        </html>
      </StoreProvider>
    </AuthenticationProvider>
  );
}
