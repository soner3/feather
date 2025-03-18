"use client";

import dynamic from "next/dynamic";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { AuthProviderProps } from "react-oidc-context";
import { WebStorageStateStore } from "oidc-client-ts";

const DynamicAuthProvider = dynamic(
  () => import("react-oidc-context").then((mod) => mod.AuthProvider),
  { ssr: false }
);

export default function AuthenticationProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();
  const [oidcConfig, setOidcConfig] = useState<AuthProviderProps | null>(null);

  useEffect(() => {
    setOidcConfig({
      authority: "http://localhost:8000/feather/authserver",
      client_id: "oidc-client",
      redirect_uri: "http://localhost:8000/callback",
      silent_redirect_uri: "http://localhost:8000/silent-renew",
      loadUserInfo: true,
      scope: "openid",
      automaticSilentRenew: true,
      userStore: new WebStorageStateStore({ store: window.localStorage }),
      onSigninCallback: () => {
        router.push("/");
      },
    });
  }, [router]);

  if (!oidcConfig) return null;

  return <DynamicAuthProvider {...oidcConfig}>{children}</DynamicAuthProvider>;
}
