"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";

export default function ExpandUrl({ params }: { params: { shortCode: string } }) {
  const router = useRouter();
  const [originalUrl, setOriginalUrl] = useState("");

  useEffect(() => {
    const fetchOriginalUrl = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/${params.shortCode}`);
        if (response.data.originalUrl) {
          window.location.href = response.data.originalUrl; // Redirect
        } else {
          setOriginalUrl("URL Not Found");
        }
      } catch (error) {
        console.error("Error fetching original URL:", error);
        setOriginalUrl("Error fetching URL");
      }
    };

    fetchOriginalUrl();
  }, [params.shortCode]);

  return (
      <div className="flex justify-center items-center h-screen text-xl">
        {originalUrl || "Redirecting..."}
      </div>
  );
}
