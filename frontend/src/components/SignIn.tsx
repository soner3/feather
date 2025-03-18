import React from "react";
import { hasAuthParams, useAuth } from "react-oidc-context";

function App() {
  const auth = useAuth();

  const [hasTriedSignin, setHasTriedSignin] = React.useState(false);

  // automatically sign-in
  React.useEffect(() => {
    if (
      !hasAuthParams() &&
      !auth.isAuthenticated &&
      !auth.activeNavigator &&
      !auth.isLoading &&
      !hasTriedSignin
    ) {
      auth.signinRedirect();
      setHasTriedSignin(true);
    }
  }, [auth, hasTriedSignin]);

  switch (auth.activeNavigator) {
    case "signinSilent":
      return <div>Signing you in...</div>;
    case "signoutRedirect":
      return <div>Signing you out...</div>;
  }

  if (auth.isLoading) {
    return <div>Loading...</div>;
  }

  if (auth.error) {
    return <div>Oops... {auth.error.message}</div>;
  }

  if (auth.isLoading) {
    return <div>Signing you in/out...</div>;
  }

  if (!auth.isAuthenticated) {
    return <div>Unable to log in</div>;
  }

  if (auth.isAuthenticated) {
    return (
      <>
        <p>Access Token: {auth.user?.access_token}</p>
        <div>
          <br />
          <p>Hello {auth.user?.profile.sub}</p>
          <br />
          <button onClick={() => auth.removeUser()}>Log out</button>
        </div>
      </>
    );
  }

  return <button onClick={() => auth.signinRedirect()}>Log in</button>;
}

export default App;
