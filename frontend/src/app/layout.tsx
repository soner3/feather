import AuthenticationProvider from "@/components/AuthenticationProvider";
import "./globals.css";
import StoreProvider from "./StoreProvider";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
        <StoreProvider>
          <AuthenticationProvider>{children}</AuthenticationProvider>
        </StoreProvider>
      </body>
    </html>
  );
}
