import * as React from "react";

export interface BadgeProps extends React.HTMLAttributes<HTMLDivElement> {
  variant?: "success" | "destructive"; 
}

function Badge({ className = "", variant = "success", ...props }: BadgeProps) {
  // Base styles for the badge
  const baseClasses =
    "inline-flex items-center justify-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 w-32"; // w-32 sets a fixed width of 8rem

  // Define styles for each variant
  const variantClasses =
    variant === "success"
      ? "bg-green-500 text-white hover:bg-green-600 border-transparent"
      : variant === "destructive"
      ? "bg-red-500 text-white hover:bg-red-600 border-transparent"
      : "";

  const combinedClasses = `${baseClasses} ${variantClasses} ${className}`;

  return <div className={combinedClasses} {...props} />;
}

export { Badge };