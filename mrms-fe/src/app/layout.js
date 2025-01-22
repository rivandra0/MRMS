import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";

//layout.js
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";

import AddBootstrap from "./addBootstrap";

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

export const metadata = {
    title: "MRMS",
    description: "Created By Muhammad Rivandra",
};

export default function RootLayout({ children }) {
    return (
        <html lang="en">
            <body className={`${geistSans.variable} ${geistMono.variable}  `}>{children}</body>
        </html>
    );
}
