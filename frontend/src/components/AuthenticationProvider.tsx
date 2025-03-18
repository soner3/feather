"use client";

import dynamic from "next/dynamic";

import { useRouter } from "next/navigation";
import { AuthProviderProps } from "oidc-react";

const DynamicAuthProvider = dynamic(
  () => import("oidc-react").then((mod) => mod.AuthProvider),
  { ssr: false }
);

export default function AuthenticationProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();
  const oidcConfig: AuthProviderProps = {
    onSignIn: () => {
      router.push("/");
    },
    authority: "http://localhost:8000/feather/authserver",
    clientId: "oidc-client",
    redirectUri: "http://localhost:8000/callback",
    silentRedirectUri: "http://localhost:8000/silent-renew",
    loadUserInfo: true,
    scope: "openid",
    automaticSilentRenew: true,
    autoSignIn: true,
  };
  return <DynamicAuthProvider {...oidcConfig}>{children}</DynamicAuthProvider>;
}
