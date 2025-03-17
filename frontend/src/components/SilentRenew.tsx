"use client";

import { useAuth } from "oidc-react";
import { useEffect } from "react";

export default function SilentRenew() {
  const { userManager } = useAuth();
  useEffect(() => {
    userManager.signinSilentCallback();
  }, [userManager]);

  return <div>Silent Renew in Progress...</div>;
}
