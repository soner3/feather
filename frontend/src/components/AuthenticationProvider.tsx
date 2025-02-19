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
    authority: "http://localhost:9000",
    clientId: "oidc-client",
    redirectUri: "http://localhost:8000/callback",
    scope: "openid profile email",
    autoSignIn: false,
  };
  return <DynamicAuthProvider {...oidcConfig}>{children}</DynamicAuthProvider>;
}
