"use client";

// import dynamic from "next/dynamic";
import { useEffect, useState } from "react";
import { AuthProvider, AuthProviderProps } from "react-oidc-context";
import { WebStorageStateStore } from "oidc-client-ts";

// const DynamicAuthProvider = dynamic(
//   () => import("react-oidc-context").then((mod) => mod.AuthProvider),
//   { ssr: false }
// );

export default function AuthenticationProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [oidcConfig, setOidcConfig] = useState<AuthProviderProps | null>(null);

  useEffect(() => {
    setOidcConfig({
      authority: "http://localhost:9000",
      client_id: "oidc-client",
      redirect_uri: "http://localhost:8000/callback",
      silent_redirect_uri: "http://localhost:8000/silent-renew",
      post_logout_redirect_uri: "http://localhost:8000",
      loadUserInfo: true,
      scope: "openid",
      automaticSilentRenew: true,
      userStore: new WebStorageStateStore({ store: window.localStorage }),
    });
  }, []);

  if (!oidcConfig) {
    return (
      <html>
        <body></body>
      </html>
    );
  }

  return <AuthProvider {...oidcConfig}>{children}</AuthProvider>;

  // return <DynamicAuthProvider {...oidcConfig}>{children}</DynamicAuthProvider>;
}
