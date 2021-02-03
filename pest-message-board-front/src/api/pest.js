import request from '@/util/request'

export const getPestData = () => {
  return request({
    url: '/pest',
    method: 'GET'
  })
}
