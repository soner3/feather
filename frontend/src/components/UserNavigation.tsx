"use client";

import { DisclosureButton, MenuItem, MenuItems } from "@headlessui/react";
import Link from "next/link";
import { useAuth } from "react-oidc-context";

const userNavigation = [
  { name: "Your Profile", href: "#" },
  { name: "Register", href: "#" },
];

export function MobileUserNavigation() {
  const { isAuthenticated, signinRedirect, signoutRedirect } = useAuth();

  return (
    <div className="mt-3 space-y-1 px-2">
      {userNavigation.map((item) => {
        if (!isAuthenticated && item.name !== "Register") {
          return null;
        }
        if (isAuthenticated && item.name === "Register") {
          return null;
        }

        return (
          <DisclosureButton
            key={item.name}
            as="a"
            href={item.href}
            className="block rounded-md px-3 py-2 text-base font-medium text-gray-400 hover:bg-gray-700 hover:text-white"
          >
            {item.name}
          </DisclosureButton>
        );
      })}
      <DisclosureButton
        as="button"
        className="block cursor-pointer w-full rounded-md px-3 py-2 text-base text-left font-medium text-gray-400 hover:bg-gray-700 hover:text-white"
        onClick={
          isAuthenticated ? () => signoutRedirect() : () => signinRedirect()
        }
      >
        {isAuthenticated ? "Sign out" : "Sign in"}
      </DisclosureButton>
    </div>
  );
}

export default function UserNavigation() {
  const { isAuthenticated, signinRedirect, signoutRedirect } = useAuth();

  return (
    <MenuItems
      transition
      className="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 ring-1 shadow-lg ring-black/5 transition focus:outline-hidden data-closed:scale-95 data-closed:transform data-closed:opacity-0 data-enter:duration-100 data-enter:ease-out data-leave:duration-75 data-leave:ease-in"
    >
      {userNavigation.map((item) => {
        if (!isAuthenticated && item.name !== "Register") {
          return null;
        }
        if (isAuthenticated && item.name === "Register") {
          return null;
        }

        return (
          <MenuItem key={item.name}>
            <Link
              href={item.href}
              className="block px-4 py-2 text-sm text-gray-700 data-focus:bg-gray-100 data-focus:outline-hidden"
            >
              {item.name}
            </Link>
          </MenuItem>
        );
      })}
      <MenuItem>
        <button
          className="block px-4 cursor-pointer w-full text-left py-2 text-sm text-gray-700 data-focus:bg-gray-100 data-focus:outline-hidden"
          onClick={
            isAuthenticated ? () => signoutRedirect() : () => signinRedirect()
          }
        >
          {isAuthenticated ? "Sign out" : "Sign in"}
        </button>
      </MenuItem>
    </MenuItems>
  );
}
