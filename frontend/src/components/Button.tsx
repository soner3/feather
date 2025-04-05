import React, { ReactNode } from "react";

export default function Button({
  children,
  onClick,
}: {
  children: ReactNode;
  onClick?: (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent> | void
  ) => void;
}) {
  return (
    <button
      onClick={onClick ? (e) => onClick(e) : undefined}
      className="bg-gray-800 font-medium text-white px-6 py-2 rounded-md"
    >
      {children}
    </button>
  );
}
