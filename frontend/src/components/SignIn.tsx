"use client";

import { useAuth } from "oidc-react";

export default function SignIn() {
  const { signIn, signOut, userData, userManager } = useAuth();

  async function handleSilentRenew() {
    const user = await userManager.signinSilent();
    if (user) {
      userManager.storeUser(user);
    }

    // console.log("Neuer Refresh Token:", user?.access_token);
    // console.log("Neuer Access Token:", user?.refresh_token);
  }

  if (userData) {
    return (
      <div>
        <p>Access Token: {userData.access_token}</p>
        <br />
        <hr />
        <br />
        <p>Refresh Token: {userData.refresh_token}</p>
        <br />
        <p>Info: {userData.profile.sub}</p>
        <br />
        <p>Refresh Token: {userData.refresh_token}</p>
        <br />
        <button onClick={() => signOut()}>Sign Out</button>
        <button onClick={handleSilentRenew}>Refresh</button>
      </div>
    );
  }
  return <button onClick={() => signIn()}>Sign In</button>;
}
