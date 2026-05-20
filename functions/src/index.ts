/**
 * VisionFolio Cloud Functions (Mock)
 *
 * Demo-only mock functions that return hardcoded success responses.
 * No real Firebase Admin SDK initialization, no Firestore calls,
 * no purchase verification. Matches SnapFolio's function signatures
 * for client compatibility.
 */

import {onCall} from "firebase-functions/v2/https";
import {setGlobalOptions} from "firebase-functions";
import * as logger from "firebase-functions/logger";

// Region: asia-northeast3 (Seoul), matching SnapFolio.
setGlobalOptions({region: "asia-northeast3", maxInstances: 10});

/* ── Types ────────────────────────────────────────────────── */

interface ClaimWelcomeBonusRequest {
  deviceIdHash?: string;
  androidIdHash?: string;
}

interface ClaimWelcomeBonusResponse {
  success: boolean;
  creditsGranted: number;
}

interface ConsumeCsGrantResponse {
  success: boolean;
  consumed: number;
}

interface VerifyPurchaseRequest {
  platform?: string;
  token?: string;
  productId?: string;
}

interface VerifyPurchaseResponse {
  success: boolean;
  verified: boolean;
  creditsAdded: number;
}

/* ── Mock Callable Functions ──────────────────────────────── */

/**
 * Mock: Grant welcome bonus credits to a new user.
 *
 * In production this would check device_bonuses/{deviceIdHash}
 * for duplicate prevention and write to Firestore.
 * Here it simply returns a hardcoded success response.
 */
export const claimWelcomeBonus = onCall<
  ClaimWelcomeBonusRequest,
  Promise<ClaimWelcomeBonusResponse>
>({invoker: "public"}, async (request) => {
  const data = request.data ?? ({} as ClaimWelcomeBonusRequest);
  const deviceIdHash = data.deviceIdHash ?? data.androidIdHash ?? "unknown";

  logger.info("claimWelcomeBonus.mock", {deviceIdHash});

  return {
    success: true,
    creditsGranted: 30,
  };
});

/**
 * Mock: Consume CS-granted bonus credits.
 *
 * In production this would read users/{uid}.csPending from Firestore
 * and clear it. Here it returns a no-op success response.
 */
export const consumeCsGrant = onCall<
  Record<string, never>,
  Promise<ConsumeCsGrantResponse>
>({invoker: "public"}, async (_request) => {
  logger.info("consumeCsGrant.mock");

  return {
    success: true,
    consumed: 0,
  };
});

/**
 * Mock: Verify an in-app purchase and grant credits.
 *
 * In production this would verify the receipt with Google Play or
 * Apple App Store APIs and write credits to Firestore.
 * Here it returns a hardcoded success response with zero credits.
 */
export const verifyPurchase = onCall<
  VerifyPurchaseRequest,
  Promise<VerifyPurchaseResponse>
>({invoker: "public"}, async (request) => {
  const data = request.data ?? ({} as VerifyPurchaseRequest);

  logger.info("verifyPurchase.mock", {
    platform: data.platform ?? "unknown",
    productId: data.productId ?? "unknown",
  });

  return {
    success: true,
    verified: true,
    creditsAdded: 0,
  };
});
