import axios from 'axios';
import type { ResponseError } from "luna";

// /rest/plugin/<plugin key>
export const baseURL = '/rest/plugin/ru.slie.luna.plugins.graty';
const axiosClient = axios.create({baseURL});
axiosClient.interceptors.response.use(
    function(response) {
      return response;
    },
    function(error) {
      if (axios.isAxiosError<ResponseError>(error) && error.response) {
        return Promise.reject(error.response);
      }

      return Promise.reject({
        status: error.status,
        reason: error.statusText
      } as ResponseError);
    }
);

export const client = axiosClient;