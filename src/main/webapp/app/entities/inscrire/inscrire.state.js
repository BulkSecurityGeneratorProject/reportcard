(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inscrire', {
            parent: 'entity',
            url: '/inscrire?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Inscrires'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscrire/inscrires.html',
                    controller: 'InscrireController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('inscrire-detail', {
            parent: 'entity',
            url: '/inscrire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Inscrire'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscrire/inscrire-detail.html',
                    controller: 'InscrireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Inscrire', function($stateParams, Inscrire) {
                    return Inscrire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'inscrire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('inscrire-detail.edit', {
            parent: 'inscrire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscrire/inscrire-dialog.html',
                    controller: 'InscrireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inscrire', function(Inscrire) {
                            return Inscrire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscrire.new', {
            parent: 'inscrire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscrire/inscrire-dialog.html',
                    controller: 'InscrireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                annee: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inscrire', null, { reload: 'inscrire' });
                }, function() {
                    $state.go('inscrire');
                });
            }]
        })
        .state('inscrire.edit', {
            parent: 'inscrire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscrire/inscrire-dialog.html',
                    controller: 'InscrireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inscrire', function(Inscrire) {
                            return Inscrire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscrire', null, { reload: 'inscrire' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscrire.delete', {
            parent: 'inscrire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscrire/inscrire-delete-dialog.html',
                    controller: 'InscrireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Inscrire', function(Inscrire) {
                            return Inscrire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscrire', null, { reload: 'inscrire' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
