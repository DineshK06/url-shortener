"use client";

import { useState } from "react";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";

export default function ShortenForm() {
    const [originalUrl, setOriginalUrl] = useState("");
    const [shortenedUrl, setShortenedUrl] = useState("");
    const [loading, setLoading] = useState(false);

    const handleShorten = async () => {
        if (!originalUrl) {
            toast.error("Please enter a valid URL!");
            return;
        }
        setLoading(true);
        try {
            const response = await axios.post(
                "http://localhost:8080/api/shorten",
                {},
                { params: { url: originalUrl, expiryDays: 7 } }
            );
            setShortenedUrl(response.data.shortUrl);
            toast.success("Short URL generated!");
        } catch (err) {
            console.error("Error shortening URL: ", err);
            toast.error("Failed to shorten URL!");
        }
        setLoading(false);
    };

    const handleCopy = async () => {
        if (shortenedUrl) {
            await navigator.clipboard.writeText(shortenedUrl);
            toast.success("Short URL copied to clipboard!");
        }
    };

    return (
        <div className="flex flex-col items-center gap-4 p-6">
            <Toaster position="top-right" reverseOrder={false} />
            <input
                type="text"
                placeholder="Enter URL to shorten..."
                className="border p-2 rounded w-96"
                value={originalUrl}
                onChange={(e) => setOriginalUrl(e.target.value)}
            />
            <button
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 disabled:bg-gray-400"
                onClick={handleShorten}
                disabled={loading}
            >
                {loading ? "Shortening..." : "Shorten URL"}
            </button>
            {shortenedUrl && (
                <div className="mt-4 flex flex-col items-center">
                    <p className="text-green-600">Shortened URL:</p>
                    <div className="flex items-center gap-2">
                        <a href={shortenedUrl} className="underline text-blue-500"
                           target="_blank" rel="noopener noreferrer">
                        {shortenedUrl}
                    </a>
                        <button
                            className="ml-2 bg-gray-300 px-2 py-1 rounded hover:bg-gray-400"
                            onClick={handleCopy}> 📋 Copy </button>
                    </div>
                </div>
            )}
        </div>
    );
}