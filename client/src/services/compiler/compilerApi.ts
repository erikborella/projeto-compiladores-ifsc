import axiosInstance from "../axiosInstance";
import { OptimizationLevel } from "../../models/OptimizationLevel";
import { SyntaxTreeRepresentation } from "../../models/SyntaxTreeRepresentation";

const uploadCode = async (code: string): Promise<string> => {
  try {
    const response = await axiosInstance.post<string>(
      "/compiler/upload",
      code,
      {
        headers: {
          "Content-Type": "text/plain",
        },
      }
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

const getLlvmIrCode = async (
  codeId: string,
  optimizationLevel: OptimizationLevel
): Promise<string> => {
  try {
    const requestUrl = buildOptimizationUrlRequest(
      `/compiler/${codeId}/llvm/ir`,
      optimizationLevel
    );
    const response = await axiosInstance.get<string>(requestUrl);

    return response.data;
  } catch (error) {
    throw error;
  }
};

const getAsmCode = async (
  codeId: string,
  optimizationLevel: OptimizationLevel
): Promise<string> => {
  try {
    const requestUrl = buildOptimizationUrlRequest(
      `/compiler/${codeId}/asm`,
      optimizationLevel
    );
    const response = await axiosInstance.get<string>(requestUrl);

    return response.data;
  } catch (error) {
    throw error;
  }
};

const getSyntaxRepresentation = async (codeId: string): Promise<SyntaxTreeRepresentation> => {
  try {
    const requestUrl = `/compiler/${codeId}/syntax`;

    const response = await axiosInstance.get<SyntaxTreeRepresentation>(requestUrl);

    return response.data;
  } catch (error) {
    throw error;
  }
};

const getCode = async (codeId: string): Promise<string> => {
  try {
    const requestUrl = `/compiler/${codeId}`;

    const response = await axiosInstance.get<string>(requestUrl);

    return response.data;
  } catch (error) {
    throw error;
  }
}

function buildOptimizationUrlRequest(
  baseUrl: string,
  optimizationLevel: OptimizationLevel
): string {
  if (optimizationLevel === OptimizationLevel.no_opt) {
    return baseUrl;
  }

  return `${baseUrl}/opt/${optimizationLevel.toString()}`;
}

export default {
  uploadCode,
  getLlvmIrCode,
  getAsmCode,
  getSyntaxRepresentation,
  getCode,
};