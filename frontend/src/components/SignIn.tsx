"use client";

import { useAuth } from "oidc-react";

export default function SignIn() {
  const { signIn, signOut, userData, userManager } = useAuth();

  async function handleSilentSignIn() {
    const user = await userManager.signinSilent();
    if (user) {
      console.log(user.access_token);
    }
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
        <button onClick={handleSilentSignIn}>Refresh</button>
      </div>
    );
  }
  return <button onClick={() => signIn()}>Sign In</button>;
}
